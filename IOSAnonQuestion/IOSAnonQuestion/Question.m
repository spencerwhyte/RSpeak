//
//  Question.m
//  IOSAnonQuestion
//
//  Created by Spencer Whyte on 2014-03-15.
//  Copyright (c) 2014 Spencer Whyte. All rights reserved.
//

#import "Question.h"

@implementation Question

-(id)initWithText:(NSString *)text{
    if(self = [super init]){
        self.text = text;
    }
    return self;
}

@end
